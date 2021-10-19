package sanpe.plugin.gibplugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import java.util.Objects;
import java.util.Random;

public class GibPlugin extends JavaPlugin implements Listener {
	BukkitTask task = null;
	@Override
	public void onEnable() {
		//プラグイン有効化時の処理
		Objects.requireNonNull(getCommand("test")).setExecutor(new CommandHandler());
		getLogger().info("有効化されました");
		getServer().getPluginManager().registerEvents(this,this);
	}
	
	@Override
	public void onDisable() {
		//プラグイン無効化時の処理
		getLogger().info("無効化されました");
	}
	
	private class SpawnTNT extends BukkitRunnable{
		int time;//秒数
		Location location;
		World world;
		
		JavaPlugin plugin;//BukkitのAPIにアクセスするためのJavaPlugin
		BukkitTask task;//自分自身を止めるためのもの

		
		
		public SpawnTNT(JavaPlugin plugin ,int i,Location location,World world) {
			this.time = i;
			this.plugin = plugin;
			this.location = location;
			this.world = world;
			
		}
		@Override
		public void run() {
			if(time <= 0){
				//タイムアップなら
				
				plugin.getServer().getScheduler().cancelTask(task.getTaskId());//自分自身を止める
			}else{
				Location loc = location.clone();
        		Random rand = new Random();
        		int numX = rand.nextInt(20) - 10;
        		int numZ = rand.nextInt(20) - 10;
        		world.spawnEntity(loc.add(numX,60,numZ),EntityType.PRIMED_TNT);
			}
			time--;//1秒減算
		}
		
	}


	@EventHandler
    public void onHit(ProjectileHitEvent e) {
		
		Block block = e.getHitBlock();
		Location location = block.getLocation();
		System.out.println(location);
		World world = block.getWorld();
		new SpawnTNT(this ,10,location,world).runTaskTimer(this,0L,20L);
	}
}